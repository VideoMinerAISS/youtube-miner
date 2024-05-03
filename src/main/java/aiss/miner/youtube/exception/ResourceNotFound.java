package aiss.miner.youtube.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ResourceNotFound extends RuntimeException{
    public  ResourceNotFound(String message) {
        super(message + " not found");
    }
}
