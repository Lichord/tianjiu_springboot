package bjfu.lixian.controller;

import bjfu.lixian.pojo.Users;
import bjfu.lixian.pojo.vo.UsersVO;
import bjfu.lixian.service.UsersService;
import bjfu.lixian.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(value="用户业务接口",tags={"用户业务controller"})
@RequestMapping("/user")
public class UserController extends BasicController {
    @Autowired
    public UsersService service;
    @ApiOperation(value="用户上传头像",notes = "用户上传头像接口")
    @ApiImplicitParam(name="userId",value="用户ID",required = true,dataType = "String",paramType = "query")
    @PostMapping("/uploadFace")
    public JSONResult faceImage(String userId,@RequestParam("file")MultipartFile[]files)throws Exception{
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("用户id不能为空");
        }
        //头像本地保存地址
        String filePath="C:/Users/HASEE/Pictures";
        //保存到数据库的相对路径
        String uploadPathDB="/"+userId+"/face";
        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        try {
            if(files!=null&&files.length>0){

                String fileName=files[0].getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //文件上传的最终保存路径
                    String fileFacePath=filePath+uploadPathDB+"/"+fileName;
                    //数据库保存路径
                    uploadPathDB+=("/"+fileName);

                    File outFile=new File(fileFacePath);
                    if(outFile.getParentFile()!=null||outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream =new FileOutputStream(outFile);
                    inputStream=files[0].getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
              }
            }else{
                return  JSONResult.errorMsg("上传出错");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return  JSONResult.errorMsg("上传出错");
        }finally {
            if(fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        service.updateUserInfo(user);

        return JSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value="用户信息查询",notes = "用户显示信息")
    @ApiImplicitParam(name="userId",value="用户ID",required = true,dataType = "String",paramType = "query")
    @PostMapping("/query")
    public JSONResult query(String userId)throws Exception{
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("用户id不能为空");
        }
        Users user=service.queryUserInfo(userId);
        UsersVO userVO=new UsersVO();
        BeanUtils.copyProperties(user,userVO);
        return JSONResult.ok(userVO);
    }
}
