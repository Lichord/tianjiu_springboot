package bjfu.lixian.controller;

import bjfu.lixian.pojo.Users;
import bjfu.lixian.pojo.vo.UsersVO;
import bjfu.lixian.service.UsersService;
import bjfu.lixian.utils.JSONResult;
import bjfu.lixian.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.expression.UserVariable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册登录的接口",tags={"注册和登录的controller"})
public class RegistLoginController extends BasicController{

    @Autowired
    private UsersService service;
    @ApiOperation(value = "用户登录",notes="用户注册接口")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody  Users user)throws Exception{
        //判断用户名密码不为空
        if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        //判断用户名是否存在
        boolean userNameIsExist=service.queryUsernameIsExist(user.getUsername());
        //保存用户是否存在
        if(!userNameIsExist){
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            service.saveUser(user);
        }else{
            return JSONResult.errorMsg("用户名已存在");
        }
        user.setPassword("");

        UsersVO userVO=setUserToken(user);

        return JSONResult.ok(userVO);
    }
    public UsersVO setUserToken(Users user){
        String uniqueToken=UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION+":"+user.getId(),uniqueToken,1000*60*15);

        UsersVO userVo=new UsersVO();
        BeanUtils.copyProperties(user,userVo);
        userVo.setUserToken(uniqueToken);
        return userVo;
    }
    @ApiOperation(value = "用户登录",notes="用户登录接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody  Users user)throws Exception{
        //判断用户名密码不为空
        if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        //查询用户名密码是否存在
        Users result =service.queryUserIsExist(user.getUsername(),MD5Utils.getMD5Str(user.getPassword()));
        if(result!=null){
            result.setPassword("");
            UsersVO userVO=setUserToken(result);
            return JSONResult.ok(userVO);
        }else{
            return JSONResult.errorMsg("账户名或密码错误");
        }
    }

    @ApiOperation(value = "用户注销",notes="用户注销接口")
    @ApiImplicitParam(name="userId",value="用户ID",required = true,dataType = "String",paramType = "query")
    @PostMapping("/logout")
    public JSONResult logout(String userId)throws Exception{
        redis.del(USER_REDIS_SESSION+":"+userId);
        return JSONResult.ok();
    }
}
