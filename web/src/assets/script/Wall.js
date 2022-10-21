import { GameObject } from "./GameObject";


export class Wall extends GameObject {
    constructor(r, c, gamemap) {
        super();
        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
    }
    start() {

    }

    update() {
        this.render();
    }

    render() {
        this.gamemap.ctx.fillStyle = "#843900";
        this.gamemap.ctx.fillRect(this.c * this.gamemap.L, this.r * this.gamemap.L, this.gamemap.L, this.gamemap.L);
    }



}
