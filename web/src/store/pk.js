

const ModulePk = {
    state: {
        status: "matching",
        opponent_name: "",
        opponent_photo: "",
        socket: null,
        gamemap: null,
        a_id: "",
        a_sx: "",
        a_sy: "",
        b_id: "",
        b_sx: "",
        b_sy: "",
        gameObject: null,
        loser: "none",

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
        updateGameInfo(state, gameInfo) {
            state.gamemap = gameInfo.map;
            state.a_id = gameInfo.a_id;
            state.a_sx = gameInfo.a_sx;
            state.a_sy = gameInfo.a_sy;
            state.b_id = gameInfo.b_id;
            state.b_sx = gameInfo.b_sx;
            state.b_sy = gameInfo.b_sy;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject;
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