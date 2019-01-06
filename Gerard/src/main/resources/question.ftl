<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css">
<section class="section">
    <div class="container">
        <div class="field is-horizontal">
            <form action="/" method="post">
                <div class="field">
                    <label class="label" for="input">Enter the input:</label>
                    <textarea rows="5" cols="100" id="input" name="input"></textarea>
                </div>
                <div class="control">
                    <div class="select">
                        <select id="day" name="day">
                            <option value="1">Day 1</option>
                            <option value="2">Day 2</option>
                            <option value="3">Day 3</option>
                            <option value="4">Day 4</option>
                            <option value="5">Day 5</option>
                            <option value="6">Day 6</option>
                            <option value="7">Day 7</option>
                            <option value="8">Day 8</option>
                            <option value="9">Day 9</option>
                            <option value="10">Day 10</option>
                            <option value="11">Day 11</option>
                            <option value="12">Day 12</option>
                            <option value="13">Day 13</option>
                            <option value="14">Day 14</option>
                            <option value="15">Day 15</option>
                            <option value="16">Day 16</option>
                            <option value="17">Day 17</option>
                            <option value="18">Day 18</option>
                            <option value="19">Day 19</option>
                            <option value="20">Day 20</option>
                            <option value="21">Day 21</option>
                            <option value="22">Day 22</option>
                            <option value="23">Day 23</option>
                            <option value="24">Day 24</option>
                            <option value="25">Day 25</option>
                        </select>
                    </div>
                    <div class="control">
                        <label class="radio">
                            <input type="radio" name="part" value="1" checked="">
                            part 1
                        </label>
                        <label class="radio">
                            <input type="radio" name="part" value="2">
                            part 2
                        </label>
                    </div>
                    <div class="control">
                        <button class="button is-primary" type="submit">Send</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>