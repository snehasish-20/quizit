import axios from 'axios'

const baseUrl = "http://localhost:9093/users";

const bearerToken = localStorage.getItem("token");

var config ={
    headers:{
        "Authorization": `Bearer ${bearerToken}`
    }
}

class UserService {

    /**
     * Retrieves all users.
     *
     * @returns {Promise} A promise that resolves to the list of users.
     */
    getAllUsers = async () =>{
        return axios.get(`${baseUrl}/getall`,config);
    }

    /**
     * Retrieves a user by their user ID.
     *
     * @param {string} userId - The ID of the user to retrieve.
     * @returns {Promise} A promise that resolves to the user object.
     */
    getUserById = async (userId) =>{
        config ={...config,params:{userId:userId}}
        return axios.get(`${baseUrl}`,config);
    }

    /**
     * Registers a new user.
     *
     * @param {object} userData - The data of the user to register.
     * @returns {Promise} A promise that resolves to the registration result.
     */
    registerUser = async (userData) =>{
        return axios.post(`${baseUrl}/register`, userData);
    }

    /**
     * Logs in a user.
     *
     * @param {string} email - The email of the user.
     * @param {string} password - The password of the user.
     * @returns {Promise} A promise that resolves to the login result.
     */
    loginUser = async(email, password) => {
        const loginData = {
            "email": email,
            "password": password
        }
        return axios.post(`${baseUrl}/login`, loginData);
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new UserService();