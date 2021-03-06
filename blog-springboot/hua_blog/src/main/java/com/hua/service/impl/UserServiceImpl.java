package com.hua.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.constant.CommonConst;
import com.hua.common.enums.FilePathEnum;
import com.hua.common.enums.RoleEnum;
import com.hua.common.exception.ServiceException;
import com.hua.mapper.UserMapper;
import com.hua.mapper.UserRoleMapper;
import com.hua.pojo.dto.EmailDTO;
import com.hua.pojo.dto.UserDetailsDTO;
import com.hua.pojo.entity.User;
import com.hua.pojo.entity.UserRole;
import com.hua.pojo.vo.*;
import com.hua.pojo.vo.param.*;
import com.hua.service.UserRoleService;
import com.hua.service.UserService;
import com.hua.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.*;
import static com.hua.common.constant.MqPrefixConst.EMAIL_EXCHANGE;
import static com.hua.common.constant.RedisPrefixConst.*;
import static com.hua.common.enums.UserAreaTypeEnum.getUserAreaType;
import static com.hua.util.CommonUtils.checkEmail;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 23:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void sendCode(String username) {
        if (!checkEmail(username)) {
            throw new ServiceException("????????????????????????");
        }
        // ?????????????????????????????????
        String code = CommonUtils.getRandomCode();
        // ???????????????
        EmailDTO emailDTO = EmailDTO.builder()
                .email(username)
                .subject("Hua???????????????????????????")
                .content("??????????????????: \n\n"
                        + "<h1><span style='background-color: #000;color: #fff;padding: 5px'>"
                        + code + "</span></h1>"
                        + "\n\n????????????<font color='red'>5</font>???????????????????????????")
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*",
                new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // ??????????????????redis?????????????????????5??????
        redisUtils.set(CODE_KEY + username, code, CODE_EXPIRE_TIME);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterParam registerParam) {
        // ????????????????????????
        if (checkUser(registerParam)) {
            throw new ServiceException("???????????????");
        }
        // ??????????????????
        User user = User.builder()
                .email(registerParam.getUsername())
                .password(BCrypt.hashpw(registerParam.getPassword(), BCrypt.gensalt()))
                .avatar(CommonConst.DEFAULT_AVATAR)
                .nickname(CommonConst.DEFAULT_NICKNAME)
                .build();
        userMapper.insert(user);
        // ??????????????????
        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void forgetPassword(RegisterParam registerParam) {
        // ????????????????????????
        if (!checkUser(registerParam)) {
            throw new ServiceException("????????????????????????????????????");
        }
        // ???????????????????????????
        registerParam.setPassword(BCrypt.hashpw(registerParam.getPassword(), BCrypt.gensalt()));
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, registerParam.getPassword())
                .eq(User::getEmail, registerParam.getUsername()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateEmail(RegisterParam registerParam) {
        String code = (String) redisUtils.get(CODE_KEY + registerParam.getUsername());
        if (!registerParam.getCode().equalsIgnoreCase(code)) {
            throw new ServiceException("??????????????????");
        }
        User user = User.builder()
                .id(UserUtils.getLoginUser().getId())
                .email(registerParam.getUsername())
                .build();
        userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoParam userInfoParam) {
        User user = User.builder()
                .id(UserUtils.getLoginUser().getId())
                .nickname(userInfoParam.getNickname())
                .intro(userInfoParam.getIntro())
                .webSite(userInfoParam.getWebSite())
                .build();
        userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        String avatar = QiniuUtils.upload(file, FilePathEnum.AVATAR.getPath());
        User user = User.builder()
                .id(UserUtils.getLoginUser().getId())
                .avatar(avatar)
                .build();
        userMapper.updateById(user);
        return avatar;
    }

    @Override
    public List<UserAreaVO> listUserAreas(QueryParam queryParam) {
        List<UserAreaVO> userAreaVOList = new ArrayList<>();
        switch (Objects.requireNonNull(getUserAreaType(queryParam.getType()))) {
            case USER:
                // ??????????????????????????????
                Object userArea = redisUtils.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaVOList = JSON.parseObject(userArea.toString(), List.class);
                }
                return userAreaVOList;
            case VISITOR:
                // ????????????????????????
                Map<Object, Object> visitorArea = redisUtils.hmGet(VISITOR_AREA);
                if (Objects.nonNull(visitorArea)) {
                    userAreaVOList = visitorArea.entrySet().stream()
                            .map(item -> UserAreaVO.builder()
                                    .name(String.valueOf(item.getKey()))
                                    .value(Long.valueOf(String.valueOf(item.getValue())))
                                    .build())
                            .collect(Collectors.toList());
                }
                return userAreaVOList;
            default:
                break;
        }
        return null;
    }

    @Override
    public PageResult<UserBackVO> listBackUser(QueryParam queryParam) {
        // ????????????????????????
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .like(!StringUtils.isEmpty(queryParam.getKeywords()), User::getNickname,
                        queryParam.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // ????????????????????????
        List<UserBackVO> userBackVOList =
                userMapper.listUser(PageUtils.getLimitCurrent(), PageUtils.getSize(), queryParam);
        return new PageResult<>(userBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisableStatus(UserDisableParam userDisableParam) {
        // ????????????????????????
        User user = User.builder()
                .id(userDisableParam.getId())
                .isDisable(userDisableParam.getIsDisable())
                .build();
        userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleParam userRoleParam) {
        // ??????????????????
        User user = User.builder()
                .id(userRoleParam.getUserId())
                .nickname(userRoleParam.getNickname())
                .build();
        userMapper.updateById(user);
        // ??????????????????
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleParam.getUserId()));
        // ??????????????????
        List<UserRole> userRoleList = userRoleParam.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleParam.getUserId())
                        .build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    public PageResult<UserOnlineVO> listOnlineUser(QueryParam queryParam) {
        // ??????security?????????session
        List<UserOnlineVO> userOnlineVOList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineVO.class))
                .filter(item -> StringUtils.isEmpty(queryParam.getKeywords())
                        || item.getNickname().contains(queryParam.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineVO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // ??????
        int fromIndex = PageUtils.getLimitCurrent().intValue();
        int size = PageUtils.getSize().intValue();
        int toIndex = userOnlineVOList.size() - fromIndex > size
                ? fromIndex + size
                : userOnlineVOList.size();
        List<UserOnlineVO> userOnlineList = userOnlineVOList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, userOnlineVOList.size());
    }

    @Override
    public void removeOnlineUser(Integer userId) {
        // ????????????session
        List<Object> userInfoList =
                sessionRegistry.getAllPrincipals().stream().filter(item -> {
                    UserDetailsDTO userDetailsDTO = (UserDetailsDTO) item;
                    return userDetailsDTO.getId().equals(userId);
                }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(item ->
                allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        // ??????session
        allSessions.forEach(SessionInformation::expireNow);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAdminPassword(PasswordParam passwordParam) {
        // ???????????????????????????
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, UserUtils.getLoginUser().getId()));
        // ?????????????????????????????????????????????????????????
        if (Objects.nonNull(existUser)
                && BCrypt.checkpw(passwordParam.getOldPassword(), existUser.getPassword())) {
            User user = User.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(passwordParam.getNewPassword(), BCrypt.gensalt()))
                    .build();
            userMapper.updateById(user);
        } else {
            throw new ServiceException("??????????????????");
        }
    }

    /**
     * ??????????????????
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserArea() {
        // ????????????????????????
        Map<String, Long> userAreaMap = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .select(User::getIpSource))
                .stream()
                .map(item -> {
                    if (StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource().substring(0, 2)
                                .replaceAll(PROVINCE, "")
                                .replaceAll(CITY, "");
                    }
                    return UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // ????????????
        List<UserAreaVO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaVO.builder()
                    .name(item.getKey())
                    .value(item.getValue())
                    .build())
                .collect(Collectors.toList());
        redisUtils.set(USER_AREA, JSON.toJSONString(userAreaList));
    }

    /**
     * ??????????????????????????????
     * @param registerParam ????????????
     * @return ????????????
     */
    private boolean checkUser(RegisterParam registerParam) {
        String code = (String) redisUtils.get(CODE_KEY + registerParam.getUsername());
        if (!registerParam.getCode().equalsIgnoreCase(code)) {
            throw new ServiceException("??????????????????");
        }
        // ????????????????????????
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getEmail, registerParam.getUsername()));
        return Objects.nonNull(user);
    }

}
