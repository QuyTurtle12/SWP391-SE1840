// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCSXtGvA5NXa0bD3-78-u-g9s1NhW0XuCI",
  authDomain: "swp291-7bd29.firebaseapp.com",
  projectId: "swp291-7bd29",
  storageBucket: "swp291-7bd29.appspot.com",
  messagingSenderId: "42730576848",
  appId: "1:42730576848:web:40f481c421c12f40af37d5",
  measurementId: "G-JJJ5Z7X88S"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);