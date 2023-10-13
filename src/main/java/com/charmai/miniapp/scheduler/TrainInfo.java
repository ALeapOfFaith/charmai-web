package com.charmai.miniapp.scheduler;

public class TrainInfo {
    /**
     * 错误信息
     */
    private String error;
    /**
     * 是否在训练
     */
    private boolean isTraining;
    /**
     * lora模型名称
     */
    private String loraName;
    private String loraPath;
    /**
     * 训练进度
     */
    private Double progress;
    /**
     * 训练是否成功
     */
    private Boolean success;

    public String getLoraPath() {
        return loraPath;
    }

    public void setLoraPath(String loraPath) {
        this.loraPath = loraPath;
    }

    public String getError() { return error; }
    public void setError(String value) { this.error = value; }

    public Boolean getIsTraining() { return isTraining; }
    public void setIsTraining(Boolean value) { this.isTraining = value; }

    public String getLoraName() { return loraName; }
    public void setLoraName(String value) { this.loraName = value; }

    public Double getProgress() { return progress; }
    public void setProgress(Double value) { this.progress = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }

    @Override
    public String toString() {
        return "TrainInfo{" +
                "error='" + error + '\'' +
                ", isTraining=" + isTraining +
                ", loraName='" + loraName + '\'' +
                ", progress=" + progress +
                ", success=" + success +
                '}';
    }
}
