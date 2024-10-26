package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.validations;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.dtos.ResponseDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;

public class UserValidations {

    public ResponseDTO validate(User user){
        ResponseDTO response = new ResponseDTO();

        response.setNumOfErrors(0);
        if (
            user.getEmail() == null ||
            !user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        ){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("El campo email no es correcto");
        }

        if(
                user.getPassword() == null ||
                !user.getPassword().matches("^.{4,16}$")
        ){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("La contraseña debe tener entre 8 y 16 caracteres, al menos un número, una minúscula y una mayúscula.");
        }

        return response;
    }
}
