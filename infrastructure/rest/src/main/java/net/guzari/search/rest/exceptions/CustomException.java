package net.guzari.search.rest.exceptions;

import lombok.Getter;
import lombok.Setter;
import net.guzari.search.openapi.model.ErrorDto;

@Getter
@Setter
public class CustomException extends AbstractCustomException {
    public CustomException(ErrorDto error) {
        super(error);
    }
}
