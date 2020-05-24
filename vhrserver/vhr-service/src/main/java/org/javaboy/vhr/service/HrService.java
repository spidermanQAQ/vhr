package org.javaboy.vhr.service;


import org.javaboy.vhr.mapper.HrMapper;
import org.javaboy.vhr.mapper.HrRoleMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    @Autowired
    HrRoleMapper hrRoleMapper;
    //认证时通过username查询数据库有无用户信息，有则返回userdetail信息(补充权限信息)，这里的hr已经继承了
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr=hrMapper.loadUserByUsername(username);
        if (hr==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //权限控制，用户登录成功，获取他的对应角色
        hr.setRoles(hrMapper.getHrRolesById(hr.getId()));
        return hr;
    }

    public List<Hr> getAllHrs(String keywords) {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId(),keywords);
    }

    /*先要删除再插入，用事务*/
    @Transactional
    public boolean updateRoles(Integer hrid, Integer[] rids) {
         hrRoleMapper.deleteRole(hrid);
         return hrRoleMapper.addRole(hrid,rids)==rids.length;

    }

    public Integer updateHr(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    public Integer deleteHr(Integer id) {
        return hrMapper.deleteByPrimaryKey(id);
    }

    public List<Hr> getAllHrsExcludeCurrentHr() {
        return hrMapper.getAllHrsExcludeCurrentHr(HrUtils.getCurrentHr().getId());
    }
}
