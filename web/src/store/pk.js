

const ModulePk = {
    state: {
        status: "matching",
        opponent_name: "",
        opponent_photo: "",
        socket: null,
        gamemap: null,

    },
    getters: {
    },
    mutations: {
        updateOppnent(state, opponent) {
            state.opponent_name = opponent.name;
            state.opponent_photo = opponent.photo;
        },
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateGameMap(state, gamemap) {
            state.gamemap = gamemap;
        },
        updateStatus(state, status) {
            state.status = status;
        }

    },
    actions: {




    },
    modules: {

    }
}

export default ModulePk;