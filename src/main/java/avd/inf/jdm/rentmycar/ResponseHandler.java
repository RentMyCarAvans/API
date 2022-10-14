package avd.inf.jdm.rentmycar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status.value());
        if(responseObj != null) map.put("data", responseObj);
        if(message != null) map.put("message", message);

        return new ResponseEntity<Object>(map,status);
    }
}
