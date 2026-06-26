package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysUserEntity;
import com.xailab.vehicle.operation.system.query.SysRoleUserQuery;
import com.xailab.vehicle.operation.system.query.SysUserQuery;
import com.xailab.vehicle.operation.system.vo.SysUserAvatarVO;
import com.xailab.vehicle.operation.system.vo.SysUserBaseVO;
import com.xailab.vehicle.operation.system.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *

 */
public interface SysUserService extends BaseService<SysUserEntity> {

    PageResult<SysUserVO> page(SysUserQuery query);

    void save(SysUserVO vo);

    void update(SysUserVO vo);

    void updateLoginInfo(SysUserBaseVO vo);

    void updateAvatar(SysUserAvatarVO avatar);

    void delete(List<Long> idList);

    /**
     * 获取用户姓名列表
     *
     * @param idList 用户ID列表
     * @return 用户姓名列表
     */
    List<String> getRealNameList(List<Long> idList);

    SysUserVO getByMobile(String mobile);

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     */
    void updatePassword(Long id, String newPassword);

    /**
     * 分配角色，用户列表
     */
    PageResult<SysUserVO> roleUserPage(SysRoleUserQuery query);

    /**
     * 批量导入用户
     *
     * @param file     excel文件
     * @param password 密码
     */
    void importByExcel(MultipartFile file, String password);

    /**
     * 导出用户信息表格
     */
    void export();

    /**
     * 获取用户信息
     * @param idList
     * @return
     */
    Map<Long,SysUserVO> getUserMap(List<Long> idList);
}
