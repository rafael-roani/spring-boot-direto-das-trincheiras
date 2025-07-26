package external.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Connection {

    private String host;

    private String username;

    private String password;

}
