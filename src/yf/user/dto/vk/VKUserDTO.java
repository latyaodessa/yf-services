package yf.user.dto.vk;

import java.util.List;

public class VKUserDTO {
	List <VKResponseDTO> response;

	public VKUserDTO() {
		super();
	}
	
	public VKUserDTO(List<VKResponseDTO> response) {
		super();
		this.response = response;
	}

	public List<VKResponseDTO> getResponse() {
		return response;
	}

	public void setResponse(List<VKResponseDTO> response) {
		this.response = response;
	}



	
}
