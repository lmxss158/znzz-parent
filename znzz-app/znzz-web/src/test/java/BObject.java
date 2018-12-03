import java.io.Serializable;

public class BObject implements Serializable {
    private String bId;
    private String bAssetCode;
    private String bAssetName;
    private String bDeviceVersion;
    private String bBorrowDepart;
    private String bCountry;

    public String getbCountry() {
        return bCountry;
    }

    public void setbCountry(String bCountry) {
        this.bCountry = bCountry;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getbAssetCode() {
        return bAssetCode;
    }

    public void setbAssetCode(String bAssetCode) {
        this.bAssetCode = bAssetCode;
    }

    public String getbAssetName() {
        return bAssetName;
    }

    public void setbAssetName(String bAssetName) {
        this.bAssetName = bAssetName;
    }

    public String getbDeviceVersion() {
        return bDeviceVersion;
    }

    public void setbDeviceVersion(String bDeviceVersion) {
        this.bDeviceVersion = bDeviceVersion;
    }

    public String getbBorrowDepart() {
        return bBorrowDepart;
    }

    public void setbBorrowDepart(String bBorrowDepart) {
        this.bBorrowDepart = bBorrowDepart;
    }

    @Override
    public String toString() {
        return "BObject{" +
                "bId='" + bId + '\'' +
                ", bAssetCode='" + bAssetCode + '\'' +
                ", bAssetName='" + bAssetName + '\'' +
                ", bDeviceVersion='" + bDeviceVersion + '\'' +
                ", bBorrowDepart='" + bBorrowDepart + '\'' +
                ", bCountry='" + bCountry + '\'' +
                '}';
    }
}
