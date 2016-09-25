package yf.user.dto.fb;

public class FBPictureDTO {
	private FBDataDTO data;
	
	public FBPictureDTO() {
		super();
	}
	public FBPictureDTO(FBDataDTO data) {
		super();
		this.data = data;
	}

	public FBDataDTO getData() {
		return data;
	}

	public void setData(FBDataDTO data) {
		this.data = data;
	}
}
