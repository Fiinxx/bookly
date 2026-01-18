package de.thws.adapter.in.api.utils;

import de.thws.domain.model.Role;
import de.thws.domain.model.User;
import de.thws.domain.port.in.LoadUserUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
public class SecurityCheck {
    @Inject
    LoadUserUseCase loadUserUseCase;

    public boolean isAuthorized(SecurityContext context, long id){
        if(context.getUserPrincipal() == null) return false;
        String username = context.getUserPrincipal().getName();
        if (username == null) return false;
        User sendingUser = loadUserUseCase.loadUserByUsername(username);
        return sendingUser.getId().equals(id);
    }

    public boolean isAuthorizedOrAdmin(SecurityContext context, long id){
        return isAuthorized(context,id) || context.isUserInRole(Role.ADMIN.toString());
    }
}
