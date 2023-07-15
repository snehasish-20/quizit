import axios from 'axios'

const baseUrl = "http://localhost:9093/quiz";

const bearerToken = localStorage.getItem("token");

var config ={
    headers:{
        Authorization: `Bearer ${bearerToken}`
    }
}

class QuizService {

    /**
     * Retrieves all quizzes.
     *
     * @returns {Promise} A promise that resolves to the response containing all quizzes.
     */
    getAllQuiz = async () =>{
        return axios.get(`${baseUrl}/getall`,config);
    }

    /**
     * Retrieves a quiz by its ID.
     *
     * @param {number} quizId - The ID of the quiz.
     * @returns {Promise} A promise that resolves to the response containing the quiz.
     */
    getQuizById = async (quizId) =>{
        config ={...config,params:{quizId:quizId}}
        return axios.get(`${baseUrl}/getquiz`,config);
    }

    /**
     * Retrieves the submissions for a specific quiz.
     *
     * @param {number} quizId - The ID of the quiz.
     * @returns {Promise} A promise that resolves to the response containing the submissions.
     */
    getQuizSubmissions = async (quizId) =>{
        config ={...config,params:{quizId:quizId}}
        return axios.get(`${baseUrl}/submissions`,config);
    }

    /**
     * Creates a new quiz.
     *
     * @param {Object} quizData - The data of the quiz to be created.
     * @returns {Promise} A promise that resolves to the response containing the created quiz.
     */
    createQuiz = async (quizData) =>{
        return axios.post(`${baseUrl}/create`, quizData, config);
    }

    /**
     * Updates an existing quiz.
     *
     * @param {Object} quizData - The updated data of the quiz.
     * @returns {Promise} A promise that resolves to the response containing the updated quiz.
     */
    updateQuiz = async (quizData) =>{
        return axios.put(`${baseUrl}/update`, quizData, config);
    }

    /**
     * Deletes a quiz by its ID.
     *
     * @param {number} quizId - The ID of the quiz to delete.
     * @returns {Promise} A promise that resolves to the response of the delete operation.
     */
    deleteQuiz = async (quizId) =>{
        config ={...config,params:{quizId:quizId}}
        return axios.delete(`${baseUrl}/delete`, config);
    }

    /**
     * Submits a quiz.
     *
     * @param {number} quizId - The ID of the quiz to submit.
     * @param {object} submission - The submission data.
     * @returns {Promise} A promise that resolves to the response of the submit operation.
     */
    submitQuiz = async (quizId, submission) => {
        config ={...config,params:{quizId:quizId}}
        return axios.post(`${baseUrl}/submit`, submission, config);
    }

    /**
     * Retrieves the admin statistics.
     *
     * @returns {Promise} A promise that resolves to the admin statistics.
     */
    getAdminStats = async () => {
        return axios.get(`${baseUrl}/admin/getstats`, config);
    }

    /**
     * Retrieves the user statistics.
     *
     * @returns {Promise} A promise that resolves to the user statistics.
     */
    getUserStats = async () => {
        return axios.get(`${baseUrl}/user/getstats`, config);
    }
    
    /**
     * Retrieves the quiz submissions by user ID.
     *
     * @returns {Promise} A promise that resolves to the quiz submissions.
     */
    getQuizSubmissionsByUserId = async () => {
        return axios.get(`${baseUrl}/user/submissions`, config);
    }
}

// eslint-disable-next-line import/no-anonymous-default-export
export default new QuizService();