const TOKEN_KEY = "token";

const saveToken = (token) => {
  localStorage.setItem(TOKEN_KEY, token);
};

const getToken = () => {
  return localStorage.getItem(TOKEN_KEY);
};

const removeToken = () => {
  localStorage.removeItem(TOKEN_KEY);
};
const getUserRole = () => {
  // Logic to decode token and retrieve user role
  const token = getToken();
  if (token) {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.roleID; // Assuming role is stored in the token payload
  }
  return null;
};
export { saveToken, getToken, getUserRole, removeToken };
