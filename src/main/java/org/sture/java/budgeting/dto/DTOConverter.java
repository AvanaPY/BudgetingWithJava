package org.sture.java.budgeting.dto;

import org.sture.java.budgeting.utils.DTO;

import java.util.List;

public interface DTOConverter<T, TDto extends DTO<T>> {
    TDto ConvertToDTO(T t);
    TDto[] DoMagicToDTO(List<TDto> lst);
    T ConvertFromDTO(TDto t);
    T[] DoMagicFromDTO(List<T> lst);

}
