package danik.test.New.selemiun.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MsgDTO implements Serializable {

    private String url;
    private String body;
    private Integer statusCode;
    private String createdDate;

}
