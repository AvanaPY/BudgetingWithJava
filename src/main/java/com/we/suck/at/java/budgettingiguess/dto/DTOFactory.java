package com.we.suck.at.java.budgettingiguess.dto;

import com.we.suck.at.java.budgettingiguess.utils.DTO;

import java.util.List;

public interface DTOFactory<T, TDto extends DTO<T>> {
    TDto ConvertToDTO(T t);
    TDto[] DoMagicToDTO(List<TDto> lst);
    T ConvertFromDTO(TDto t);
    T[] DoMagicFromDTO(List<T> lst);

}
