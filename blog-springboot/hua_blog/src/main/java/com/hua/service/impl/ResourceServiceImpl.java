package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.common.exception.ServiceException;
import com.hua.common.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.hua.mapper.ResourceMapper;
import com.hua.mapper.RoleResourceMapper;
import com.hua.pojo.entity.Resource;
import com.hua.pojo.entity.RoleResource;
import com.hua.pojo.vo.LabelOptionVO;
import com.hua.pojo.vo.ResourceVO;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.pojo.vo.param.ResourceParam;
import com.hua.service.ResourceService;
import com.hua.util.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.FALSE;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/19 17:32
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importSwagger() {
        // 删除所有资源
        this.remove(null);
        roleResourceMapper.delete(null);
        List<Resource> resourceList = new ArrayList<>();
        Map<String, Object> data =
                restTemplate.getForObject("http://localhost:8080/v2/api-docs", Map.class);
        // 获取所有模块
        List<Map<String, String>> tagsList = (List<Map<String, String>>) data.get("tags");
        tagsList.forEach(tags -> {
            Resource resource = Resource.builder()
                    .resourceName(tags.get("name"))
                    .isAnonymous(FALSE)
                    .build();
            resourceList.add(resource);
        });
        this.saveBatch(resourceList);
        Map<String, Integer> permissionMap = resourceList.stream()
                .collect(Collectors.toMap(Resource::getResourceName, Resource::getId));
        resourceList.clear();
        // 获取所有接口
        Map<String, Map<String, Map<String, Object>>> paths =
                (Map<String, Map<String, Map<String, Object>>>) data.get("paths");
        paths.forEach((url, value) -> value.forEach((requestMethod, info) -> {
            String permissionName = info.get("summary").toString();
            List<String> tags = (List<String>) info.get("tags");
            Integer parentId = permissionMap.get(tags.get(0));
            Resource resource = Resource.builder()
                    .resourceName(permissionName)
                    .url(url.replaceAll("\\{[^}]*\\}", "*"))
                    .parentId(parentId)
                    .requestMethod(requestMethod.toUpperCase())
                    .isAnonymous(FALSE)
                    .build();
            resourceList.add(resource);
        }));
        this.saveBatch(resourceList);
    }

    @Override
    public List<ResourceVO> listResource(QueryParam queryParam) {
        // 查看资源列表
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .like(StringUtils.isNotBlank(queryParam.getKeywords()), Resource::getResourceName,
                        queryParam.getKeywords()));
        // 获取所有模块
        List<Resource> parentList = listResourceModule(resourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 绑定模块下的所有接口
        List<ResourceVO> resourceVOList = parentList.stream().map(item -> {
            ResourceVO resourceVO = BeanCopyUtils.copyObject(item, ResourceVO.class);
            List<ResourceVO> childrenList =
                    BeanCopyUtils.copyList(childrenMap.get(item.getId()), ResourceVO.class);
            resourceVO.setChildren(childrenList);
            childrenMap.remove(item.getId());
            return resourceVO;
        }).collect(Collectors.toList());
        // 若还有资源未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Resource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceVO> childrenVOList = childrenList.stream()
                    .map(item -> BeanCopyUtils.copyObject(item, ResourceVO.class))
                    .collect(Collectors.toList());
            resourceVOList.addAll(childrenVOList);
        }
        return resourceVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateResource(ResourceParam resourceParam) {
        // 更新资源信息
        Resource resource = BeanCopyUtils.copyObject(resourceParam, Resource.class);
        this.saveOrUpdate(resource);
        // 重新加载角色资源信息
        filterInvocationSecurityMetadataSource.clearDataSource();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteResource(Integer resourceId) {
        // 查询是否有角色关联
        Integer count = roleResourceMapper.selectCount(new LambdaQueryWrapper<RoleResource>()
                .eq(RoleResource::getResourceId, resourceId));
        if (count > 0) {
            throw new ServiceException("该资源下存在角色");
        }
        // 删除子资源
        List<Integer> resourceIdList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId)
                .eq(Resource::getParentId, resourceId))
                .stream()
                .map(Resource::getId)
                .collect(Collectors.toList());
        resourceIdList.add(resourceId);
        resourceMapper.deleteBatchIds(resourceIdList);
    }

    @Override
    public List<LabelOptionVO> listResourceOption() {
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName, Resource::getParentId)
                .eq(Resource::getIsAnonymous, FALSE));
        // 获取所有模块
        List<Resource> parentList = listResourceModule(resourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 组装父子数据
        return parentList.stream().map(item -> {
            List<LabelOptionVO> list = new ArrayList<>();
            List<Resource> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .map(resource -> LabelOptionVO.builder()
                                .id(resource.getId())
                                .label(resource.getResourceName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionVO.builder()
                    .id(item.getId())
                    .label(item.getResourceName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 获取模块下的所有资源
     * @param resourceList 资源列表
     * @return 模块资源
     */
    private Map<Integer, List<Resource>> listResourceChildren(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(resource -> Objects.nonNull(resource.getParentId()))
                .collect(Collectors.groupingBy(Resource::getParentId));
    }

    /**
     * 获取所有资源模块
     * @param resourceList 资源列表
     * @return 资源模块列表
     */
    private List<Resource> listResourceModule(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(resource -> Objects.isNull(resource.getParentId()))
                .collect(Collectors.toList());
    }
}
