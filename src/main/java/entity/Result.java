package entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Stepan
 */

@Getter
@Setter
public class Result {

    private Long count;
    private String name;

    public Result(Long count, String name){
        this.name = name;
        this.count = count;
    }

}
