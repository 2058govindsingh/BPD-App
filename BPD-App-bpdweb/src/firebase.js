import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyAehL-3ITZWqZxQOfvIgdD8T5BgqfIOHkE",
  authDomain: "authentication-firebase-d4c20.firebaseapp.com",
  projectId: "authentication-firebase-d4c20",
  storageBucket: "authentication-firebase-d4c20.appspot.com",
  messagingSenderId: "918116916613",
  appId: "1:918116916613:web:95b235d26b25f2a152f8ec",
  measurementId: "G-8VS4B2GC4Z"
};

const app = initializeApp(firebaseConfig);

const auth = getAuth();

export { app, auth };
