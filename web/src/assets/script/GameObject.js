const GAME_OBJECT = [];

export class GameObject {
    constructor() {
        GAME_OBJECT.push(this);
        this.timedelta = 0;
        this.is_start = false;
    }

    start() {

    }

    update() {

    }
    on_destroy() {

    }

    destroy() {
        this.on_destroy();

        for (let i in GAME_OBJECT) {

            const obj = GAME_OBJECT[i];
            if (obj === this) {
                GAME_OBJECT.splice(i); //下标
                break;
            }
        }

    }
}
let last_timestamp = 0;
const step = (timestamp) => {
    for (let i in GAME_OBJECT) {
        const obj = GAME_OBJECT[i];
        if (!obj.is_start) {
            obj.is_start = true;
            obj.start();
        } else {

            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }

    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
};

requestAnimationFrame(step);