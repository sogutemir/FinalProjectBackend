package org.work.personnelinfo.resourceFile.dto;

public class ResourceFileDTO {
    private Long id;
    private byte[] data;
    private String fileName;

    public ResourceFileDTO(byte[] data, String fileName) {
        this.data = data;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
