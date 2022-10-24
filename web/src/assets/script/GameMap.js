
import { GameObject } from "./GameObject";
import { Snake } from "./Snakes";
import { Wall } from "./Wall";

export class GameMap extends GameObject {
    constructor(ctx, parent) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //一个格子的相对距离
        this.row = 13;
        this.col = 14;
        this.walls = [];
        this.blocknumber = 30;

        this.snakers = [
            new Snake({ id: 0, color: "#4876EC", r: this.row - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#F94848", r: 1, c: this.col - 2 }, this),
        ];
    }

    check(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        const dx = [0, 1, 0, -1];
        const dy = [1, 0, -1, 0];
        for (let i = 0; i < 4; i++) {
            let x = sx + dx[i];
            let y = sy + dy[i];
            if (!g[x][y] && this.check(g, x, y, tx, ty)) { //Flood Fill算法
                return true;
            }
        }
        return false;

    }

    create_walls() {
        //初始化
        const g = [];
        for (let i = 0; i < this.row; i++) {
            g[i] = [];
            for (let j = 0; j < this.col; j++) {
                g[i][j] = false;
            }
        }
        //左右
        for (let i = 0; i < this.row; i++) {
            g[i][0] = true;
        }
        for (let i = 0; i < this.row; i++) {
            g[i][this.col - 1] = true;
        }
        //上下
        for (let i = 0; i < this.col; i++) {
            g[0][i] = true;
        }
        for (let i = 0; i < this.col; i++) {
            g[this.row - 1][i] = true;
        }

        //中间
        for (let i = 1; i <= this.blocknumber / 2; i++) {
            for (let j = 0; j < 10000; j++) {
                let r = parseInt(Math.random() * this.row);
                let c = parseInt(Math.random() * this.col);
                if (r == 1 && c == this.col - 2 || c == 1 && r == this.row - 2) continue;

                if (g[r][c] || g[this.row - r - 1][this.col - c - 1]) {
                    continue;
                }
                g[r][c] = true;
                g[this.row - r - 1][this.col - c - 1] = true;
                break;
            }


        }
        const copy_g = JSON.parse(JSON.stringify(g));

        if (!this.check(copy_g, this.row - 2, 1, 1, this.col - 2)) {
            return false;
        }


        for (let i = 0; i < this.row; i++) {
            for (let j = 0; j < this.col; j++) {
                if (g[i][j]) {
                    this.walls.push(new Wall(i, j, this));
                }
            }
        }

        return true;



    }
    add_listening_events() {
        this.ctx.canvas.focus();
        const [snake0, snake1] = this.snakers;
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === "w") snake0.set_direction(0);
            else if (e.key === "d") snake0.set_direction(1);
            else if (e.key === "s") snake0.set_direction(2);
            else if (e.key === "a") snake0.set_direction(3);
            else if (e.key === "ArrowUp") snake1.set_direction(0);
            else if (e.key === "ArrowRight") snake1.set_direction(1);
            else if (e.key === "ArrowDown") snake1.set_direction(2);
            else if (e.key === "ArrowLeft") snake1.set_direction(3);
        });

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