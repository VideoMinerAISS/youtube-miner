package aiss.miner.youtube.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, List<String>> res = new HashMap<>();
        res.put("errors", errors);


        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFound.class})
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFound ex) {
        String error = ex.getMessage();
        Map<String, String> res = new HashMap<>();
        res.put("errors", error);
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> tooManyRequest(HttpClientErrorException ex){
        String error = ex.getMessage();
        Map<String, String> res = new HashMap<>();
        res.put("errors", error);
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> wrongParameterFormat(NumberFormatException ex){
        String error = "Parametros de busqueda inválidos";
        Map<String, String> res = new HashMap<>();
        res.put("errors", error);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

}
