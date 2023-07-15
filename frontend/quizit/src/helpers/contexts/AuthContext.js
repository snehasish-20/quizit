import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({children}) =>{
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const[user,setUser] = useState(storedUser);

    const login = (user) =>{
        setUser(user.user);
        localStorage.setItem("user", JSON.stringify(user.user));
        localStorage.setItem("token", user.token)
    }

    const logout = () =>{
        setUser(null);
        localStorage.clear();
    }

    return (
        <AuthContext.Provider value={{user,login,logout}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    return useContext(AuthContext);
}