import { GameObject } from "./GameObject";
import { Cell } from "./Cell";


export class Snake extends GameObject {

    constructor(info, gamemap) {
        super();
        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.cells = [new Cell(info.r, info.c)];

        this.next_cell = null;

        this.speed = 5; //速度
        this.state = "idle" //idle 禁止,move 移动中,die死去

        this.direction = -1;
        this.dx = [-1, 0, 1, 0];
        this.dy = [0, 1, 0, -1];

        this.step = 0;

        this.eps = 1e-2;
        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2;
        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ];

    }

    set_direction(d) {
        this.direction = d;

    }

    next_step() {
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dx[d], this.cells[0].c + this.dy[d]);

        this.direction = -1;
        this.state = "move";
        this.step++;

        const k = this.cells.length;//增长一个
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

        if (!this.gamemap.check_vaild(this.next_cell)) {
            this.state = "die";
            this.color = "white";
        } else {
            this.eye_direction = d;
        }

    }

    update_move() {//动态更新

        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) { //到达
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            this.state = "idle";
            if (!this.check_increace()) {//不长大
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000;
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_increace()) { //屁股也要跟着移动
                const k = this.cells.length;
                const tail = this.cells[k - 1];
                const tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;


            }
        }


    }
    check_increace() {
        if (this.step <= 10) return true;
        if ((this.step - 10) % 3 == 0) return true;
        return false;
    }


    start() {

    }

    update() {
        if (this.state === "move") {
            this.update_move();
        }

        this.render();
    }
    render() {
        const ctx = this.gamemap.ctx;
        const L = this.gamemap.L;

        ctx.fillStyle = this.color;
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L * 0.8 / 2, 0, Math.PI * 2);
            ctx.fill();
        }
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1];
            const b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) {
                continue;
            }
            if (Math.abs(a.x - b.x) < this.eps) {//x轴相等

                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);

            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++) {
            const eye_x = this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.2;
            const eye_y = this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.2;
            ctx.beginPath();
            ctx.arc(eye_x * L, eye_y * L, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }

    }


}