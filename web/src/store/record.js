
const ModuleRecord = {
    state: {
        is_record: false,
        a_username: "",
        a_photo: "",
        a_step: "",
        b_username: "",
        b_photo: "",
        b_step: "",
        record_loser: "",
    },
    getters: {
    },
    mutations: {
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateStep(state, step) {
            state.a_step = step.a_step;
            state.b_step = step.b_step;
        },
        updateRecordLoser(state, record_loser) {
            state.record_loser = record_loser;
        },
        updateRecordPlayerInfo(state, data) {
            state.a_username = data.a_username;
            state.a_photo = data.a_photo;
            state.b_username = data.b_username;
            state.b_photo = data.b_photo;
        }
    },
    actions: {

    },
    modules: {

    }
}

export default ModuleRecord;