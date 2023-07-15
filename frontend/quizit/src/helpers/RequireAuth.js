import { Navigate } from "react-router-dom";
import { useAuth } from "./contexts/AuthContext"

export const RequireAuth = ({children, role}) =>{
    const auth = useAuth();

    if(!auth.user){
        window.location.assign('/signin')
    }
    else if(role && (auth.user.roleType !== role)){
        const path = auth?.user?.roleType === "ADMIN" ? '/admin-dashboard' : '/user-dashboard';
        return <Navigate to={path}/>
    }

    return children;
}