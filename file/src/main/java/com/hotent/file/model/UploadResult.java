package com.hotent.file.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="附件上传返回结果")
public class UploadResult {
    @ApiModelProperty(name="state",notes="状态 true：上传成功  false：上传失败")
    private boolean success;

    @ApiModelProperty("附件Id")
    private String fileId;

    @ApiModelProperty("附件名称")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Long size;


    @ApiModelProperty("出错信息")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
