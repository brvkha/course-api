import axiosClient from '../api/axiosClient';

export const getAllCourses = () => {
    return axiosClient.get('/courses');
};

export const createCourse = (courseData) => {
    return axiosClient.post('/courses', courseData);
};