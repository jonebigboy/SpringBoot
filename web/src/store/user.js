import $ from 'jquery'

const ModuleUser = {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },

        updateToken(state, token) {
            state.token = token;
        },

        loginout(state) {
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = "";
        }

    },
    actions: {

        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.message === "success") {
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },

        getinfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/getinfo/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.message === "success") {
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true,
                        })
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }

            });
        },
        loginout(context) {
            context.commit("loginout");
            localStorage.removeItem("jwt_token");
        }


    },
    modules: {

    }
}

export default ModuleUser;