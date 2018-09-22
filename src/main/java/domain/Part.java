package domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Part {
    private Long id;
    private String name;
    private String number;
    private String vendor;
    private Integer qty;
    private Date shipped;
    private Date received;
}
