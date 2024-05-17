package com.project.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.MultiFileResource;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.springboot.common.Result;
import com.project.springboot.entity.FileData;
import com.project.springboot.entity.User;
import com.project.springboot.mapper.FileMapper;
import com.project.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String uploadPath;

    @Resource
    private FileMapper fileMapper;
    /**
     * 文件上传接口
     * @param file 前端传递来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String OriginFileName=file.getOriginalFilename();
        String type= FileUtil.extName(OriginFileName);
        long size=file.getSize();
        //存储到磁盘
        File uploadfile=new File(uploadPath);
        if(!uploadfile.exists()){
            uploadfile.mkdirs();
        }
        //定义文件的唯一标识码
        String uuid= IdUtil.fastSimpleUUID();
        String fileUuid=uuid+StrUtil.DOT+type;
        File uploadFile=new File(uploadPath+fileUuid);

        String md5;
        String url;
        file.transferTo(uploadFile);
        //查询文件的md5值，防止重复
        md5= SecureUtil.md5(uploadFile);
        FileData fileData=getFilesByMd5(md5);
        if(fileData!=null){
            url= fileData.getUrl();
            uploadFile.delete();
        }else{
            url="http://localhost:8082/file/"+fileUuid;
        }

        FileData saveFile=new FileData();
        saveFile.setName(OriginFileName);
        saveFile.setType(type);
        saveFile.setSize(size/1024);//kB
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 文件下载接口 http://localhost:8082/file/{uuid}
     * @param fileUuid
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile=new File(uploadPath+ fileUuid);
        ServletOutputStream os= response.getOutputStream();
        //设置输出流的格式
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUuid,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    private FileData getFilesByMd5(String md5) {
        QueryWrapper<FileData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<FileData> fileList=fileMapper.selectList(queryWrapper);
        return fileList.size()==0?null:fileList.get(0);
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum,
                       @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String name){
        QueryWrapper<FileData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_delete",false);
        queryWrapper.like(!name.equals(""),"name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(fileMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper));
    }

    /**
     * 删除接口
     */
    @DeleteMapping("/{id}")
    public Result datete(@PathVariable Integer id){
        FileData fileData=fileMapper.selectById(id);
        fileData.setTo_delete(true);
        fileMapper.updateById(fileData);
        return Result.success(fileMapper.updateById(fileData));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result batchDelete(@RequestBody List<Integer> ids){
        QueryWrapper<FileData> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<FileData> files=fileMapper.selectList(queryWrapper);
        for(FileData file:files){
            file.setTo_delete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody FileData fileData)
    {
        return Result.success(fileMapper.updateById(fileData));
    }

}
