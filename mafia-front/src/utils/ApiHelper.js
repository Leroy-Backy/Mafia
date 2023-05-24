import api from "./Api";

export function getRequestForUserForm(create, manager, user, isManager, id) {
  if(create) {
    return api.post("/api/guard", user);
  } else if (manager) {
    return api.put("/api/manager", user);
  } else if (isManager) {
    return api.put(`/api/guard/${id}`, user);
  } else {
    return api.put("/api/guard", user);
  }
}

export function getRequestForPointForm(create, point, id) {
  if(create) {
    return api.post("/api/point", point);
  } else {
    return api.put(`/api/point/${id}`, point);
  }
}