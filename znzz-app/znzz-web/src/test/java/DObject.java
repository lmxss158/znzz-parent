import java.io.Serializable;

public class DObject implements Serializable {
    private String dId;
    private String dAssetCode;
    private String dDeviceVersion;
    private String dAssetName;
    private String dBorrowDepart;
    private String dCountry;

    public String getdCountry() {
        return dCountry;
    }

    public void setdCountry(String dCountry) {
        this.dCountry = dCountry;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getdAssetCode() {
        return dAssetCode;
    }

    public void setdAssetCode(String dAssetCode) {
        this.dAssetCode = dAssetCode;
    }

    public String getdDeviceVersion() {
        return dDeviceVersion;
    }

    public void setdDeviceVersion(String dDeviceVersion) {
        this.dDeviceVersion = dDeviceVersion;
    }

    public String getdAssetName() {
        return dAssetName;
    }

    public void setdAssetName(String dAssetName) {
        this.dAssetName = dAssetName;
    }

    public String getdBorrowDepart() {
        return dBorrowDepart;
    }

    public void setdBorrowDepart(String dBorrowDepart) {
        this.dBorrowDepart = dBorrowDepart;
    }

    @Override
    public String toString() {
        return "DObject{" +
                "dId='" + dId + '\'' +
                ", dAssetCode='" + dAssetCode + '\'' +
                ", dDeviceVersion='" + dDeviceVersion + '\'' +
                ", dAssetName='" + dAssetName + '\'' +
                ", dBorrowDepart='" + dBorrowDepart + '\'' +
                '}';
    }
}
