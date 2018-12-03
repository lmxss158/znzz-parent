import java.io.Serializable;

public class CloudObject implements Serializable {

    private String cSequenceId;
    private String cAssetCode;
    private String cId;
    private String cAssetName;
    private String cDeviceVersion;
    private String cAssetGroup;

    public String getcSequenceId() {
        return cSequenceId;
    }

    public void setcSequenceId(String cSequenceId) {
        this.cSequenceId = cSequenceId;
    }

    public String getcAssetCode() {
        return cAssetCode;
    }

    public void setcAssetCode(String cAssetCode) {
        this.cAssetCode = cAssetCode;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcAssetName() {
        return cAssetName;
    }

    public void setcAssetName(String cAssetName) {
        this.cAssetName = cAssetName;
    }

    public String getcDeviceVersion() {
        return cDeviceVersion;
    }

    public void setcDeviceVersion(String cDeviceVersion) {
        this.cDeviceVersion = cDeviceVersion;
    }

    public String getcAssetGroup() {
        return cAssetGroup;
    }

    public void setcAssetGroup(String cAssetGroup) {
        this.cAssetGroup = cAssetGroup;
    }

    @Override
    public String toString() {
        return "CloudObject{" +
                "cSequenceId='" + cSequenceId + '\'' +
                ", cAssetCode='" + cAssetCode + '\'' +
                ", cId='" + cId + '\'' +
                ", cAssetName='" + cAssetName + '\'' +
                ", cDeviceVersion='" + cDeviceVersion + '\'' +
                ", cAssetGroup='" + cAssetGroup + '\'' +
                '}';
    }
}
