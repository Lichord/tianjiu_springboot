package bjfu.lixian.service.Impl;

import bjfu.lixian.mapper.UsersMapper;
import bjfu.lixian.pojo.Users;
import bjfu.lixian.service.UsersService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper mapper;
    @Autowired
    private Sid sid;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String name){
        Users user=new Users();
        user.setUsername(name);
        Users result=mapper.selectOne(user);
        return result==null ? false : true;
    }
    @Transactional(propagation = Propagation.REQUIRED)//默认值就是REQUIRED
    @Override
    public void saveUser(Users user){
        String userId=sid.nextShort();
        user.setId(userId);
        mapper.insert(user);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserIsExist(String username,String password){
        Example userExample=new Example(Users.class);
        Example.Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);
        return mapper.selectOneByExample(userExample);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user){
        Example userExample=new Example(Users.class);
        Example.Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("id",user.getId());
        //哪个属性有值就更新哪个
        mapper.updateByExampleSelective(user,userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId){
        Example userExample=new Example(Users.class);
        Example.Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        return mapper.selectOneByExample(userExample);
    }


}
