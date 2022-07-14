package Pack.vo;

import java.text.SimpleDateFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class HotlineDTO {
	String hotline_id;
    String category;
    String title;
    String writer;
    String writer_id;
    String content;
    String reg_date;
    String confirm_date;
    String status;
    

    @Builder
	public HotlineDTO(String category, String title, String writer, String content, String writer_id) {
		super();
		this.category = category;
		this.title = title;
		this.writer = writer;
		this.writer_id = writer_id;
		this.content = content;
	}
}