package org.work.personnelinfo.resourceFile.dto;

public class ResourceFileDTO {
    private byte[] data;
    private String fileName;

    public ResourceFileDTO(byte[] data, String fileName) {
        this.data = data;
        this.fileName = fileName;
    }

    // Getter ve Setter'lar
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
