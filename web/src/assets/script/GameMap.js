

import { GameObject } from "./GameObject";
import { Snake } from "./Snakes";
import { Wall } from "./Wall";

export class GameMap extends GameObject {
    constructor(ctx, parent, g, store) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //一个格子的相对距离
        this.row = 13;
        this.col = 14;
        this.walls = [];
        this.blocknumber = 10;
        this.g = g;
        this.store = store;
        this.snakers = [
            new Snake({ id: 0, color: "#4876EC", r: this.row - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#F94848", r: 1, c: this.col - 2 }, this),
        ];

    }



    create_walls() {


        for (let i = 0; i < this.row; i++) {
            for (let j = 0; j < this.col; j++) {
                if (this.g[i][j] === 1) {
                    this.walls.push(new Wall(i, j, this));
                }
            }
        }

        return true;



    }
    add_listening_events() {
        this.ctx.canvas.focus();
        let d = -1;


        if (this.store.state.record.is_record) {
            let k = 0;
            const stepA = this.store.state.record.a_step;
            const stepB = this.store.state.record.b_step;
            const loser = this.store.state.record.record_loser;
            const [snake0, snake1] = this.snakers;


            const Interval_id = setInterval(() => {
                if (k >= stepA.length - 1) {
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                        snake0.color = "white";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                        snake1.color = "white";
                    }
                    clearInterval(Interval_id);

                } else {
                    snake0.set_direction(parseInt(stepA[k]));
                    snake1.set_direction(parseInt(stepB[k]));
                }
                k++;
            }, 300);

        } else {
            this.ctx.canvas.addEventListener("keydown", e => {

                if (e.key === "w") d = 0;
                else if (e.key === "d") d = 1;
                else if (e.key === "s") d = 2;
                else if (e.key === "a") d = 3;



                if (d >= 0 && this.store.state.pk.loser === "none") {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }

            });
        }




    }

    check_rdy() {
        for (const snake of this.snakers) {

            if (snake.state !== "idle") return false;

            if (snake.direction === -1) return false;

        }
        return true;
    }

    check_vaild(cell) {
        //撞墙
        for (const i in this.walls) {
            if (this.walls[i].r === cell.r && this.walls[i].c === cell.c) {
                return false;
            }
        }

        //碰蛇
        for (const snake of this.snakers) {
            let k = snake.cells.length;

            if (!snake.check_increace()) {
                k--;//减去蛇尾 不去判断
            }

            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
                    return false;
                }
            }
        }
        return true;



    }

    start() {

        for (let i = 0; i < 1000; i++) {
            if (this.create_walls()) {
                break;
            }
        }
        this.add_listening_events();

    }
    next_step() {
        for (const snake of this.snakers) {
            snake.next_step();
        }
    }

    updata_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.col, this.parent.clientHeight / this.row));
        this.ctx.canvas.height = this.L * this.row;
        this.ctx.canvas.width = this.L * this.col;
    }

    update() {
        this.updata_size();

        if (this.check_rdy()) {
            this.next_step();
        }
        this.render();
    }
    render() {
        const color_even = "#7FB80E";
        const color_odd = "#B8D53B";
        for (let i = 0; i < this.row; i++) {
            for (let j = 0; j < this.col; j++) {
                if ((i + j) % 2 == 0) {
                    this.ctx.fillStyle = color_odd;
                } else {
                    this.ctx.fillStyle = color_even;
                }
                this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
            }
        }


    }
}