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
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
            throw new ServiceException("请输入正确的邮箱");
        }
        // 生成六位随机验证码发送
        String code = CommonUtils.getRandomCode();
        // 发送验证码
        EmailDTO emailDTO = EmailDTO.builder()
                .email(username)
                .subject("Hua的个人博客的验证码")
                .content("您的验证码为: \n\n"
                        + "<h1><span style='background-color: #000;color: #fff;padding: 5px'>"
                        + code + "</span></h1>"
                        + "\n\n有效期为<font color='red'>5</font>分钟，请尽快填写！")
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*",
                new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // 将验证码存入redis，设置过期时间5分组
        redisUtils.set(CODE_KEY + username, code, CODE_EXPIRE_TIME);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterParam registerParam) {
        // 检查账户是否合法
        if (checkUser(registerParam)) {
            throw new ServiceException("用户已注册");
        }
        // 新增用户信息
        User user = User.builder()
                .email(registerParam.getUsername())
                .password(BCrypt.hashpw(registerParam.getPassword(), BCrypt.gensalt()))
                .avatar(CommonConst.DEFAULT_AVATAR)
                .nickname(CommonConst.DEFAULT_NICKNAME)
                .build();
        userMapper.insert(user);
        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void forgetPassword(RegisterParam registerParam) {
        // 检查账户是否合法
        if (!checkUser(registerParam)) {
            throw new ServiceException("用户尚未注册，请先注册！");
        }
        // 根据用户名修改密码
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
            throw new ServiceException("验证码不正确");
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
                // 查询注册用户区域分布
                Object userArea = redisUtils.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaVOList = JSON.parseObject(userArea.toString(), List.class);
                }
                return userAreaVOList;
            case VISITOR:
                // 查询游客区域分布
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
        // 获取后台用户数量
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .like(!StringUtils.isEmpty(queryParam.getKeywords()), User::getNickname,
                        queryParam.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 获取后台用户列表
        List<UserBackVO> userBackVOList =
                userMapper.listUser(PageUtils.getLimitCurrent(), PageUtils.getSize(), queryParam);
        return new PageResult<>(userBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisableStatus(UserDisableParam userDisableParam) {
        // 更新用户禁用状态
        User user = User.builder()
                .id(userDisableParam.getId())
                .isDisable(userDisableParam.getIsDisable())
                .build();
        userMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleParam userRoleParam) {
        // 更新用户昵称
        User user = User.builder()
                .id(userRoleParam.getUserId())
                .nickname(userRoleParam.getNickname())
                .build();
        userMapper.updateById(user);
        // 删除用户角色
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleParam.getUserId()));
        // 添加用户角色
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
        // 获取security的在线session
        List<UserOnlineVO> userOnlineVOList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineVO.class))
                .filter(item -> StringUtils.isEmpty(queryParam.getKeywords())
                        || item.getNickname().contains(queryParam.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineVO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // 分页
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
        // 获取用户session
        List<Object> userInfoList =
                sessionRegistry.getAllPrincipals().stream().filter(item -> {
                    UserDetailsDTO userDetailsDTO = (UserDetailsDTO) item;
                    return userDetailsDTO.getId().equals(userId);
                }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(item ->
                allSessions.addAll(sessionRegistry.getAllSessions(item, false)));
        // 注销session
        allSessions.forEach(SessionInformation::expireNow);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAdminPassword(PasswordParam passwordParam) {
        // 查询旧密码是否正确
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, UserUtils.getLoginUser().getId()));
        // 正确则修改密码，错误则提示旧密码不正确
        if (Objects.nonNull(existUser)
                && BCrypt.checkpw(passwordParam.getOldPassword(), existUser.getPassword())) {
            User user = User.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(passwordParam.getNewPassword(), BCrypt.gensalt()))
                    .build();
            userMapper.updateById(user);
        } else {
            throw new ServiceException("旧密码不正确");
        }
    }

    /**
     * 检查用户数据是否合法
     * @param registerParam 用户数据
     * @return 合法状态
     */
    private boolean checkUser(RegisterParam registerParam) {
        String code = (String) redisUtils.get(CODE_KEY + registerParam.getUsername());
        if (!registerParam.getCode().equalsIgnoreCase(code)) {
            throw new ServiceException("验证码错误！");
        }
        // 查询账户是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getEmail, registerParam.getUsername()));
        return Objects.nonNull(user);
    }

}
