use std::collections::HashSet;
use std::str::FromStr;

#[aoc_generator(day1)]
pub fn input_generator(input: &str) -> Vec<i32> {
    input
        .lines()
        .map(|l| FromStr::from_str(l).unwrap())
        .collect()
}

#[aoc(day1, part1)]
pub fn calculate_frequency(input: &[i32]) -> i32 {
    input.iter().sum()
}

#[aoc(day1, part2)]
pub fn first_double_frequency(input: &[i32]) -> i32 {
    let mut input_cycle = input.iter().cycle();
    let mut frequency = 0;
    let mut past_frequencies = HashSet::new();
    while !past_frequencies.contains(&frequency) {
        past_frequencies.insert(frequency);
        frequency += input_cycle.next().unwrap();
    }
    frequency
}

#[cfg(test)]
mod tests {
    use super::{calculate_frequency, first_double_frequency};

    #[test]
    fn sample1() {
        assert_eq!(3, calculate_frequency(&vec!(1, 1, 1)));
    }

    #[test]
    fn sample2() {
        assert_eq!(0, calculate_frequency(&vec!(1, 1, -2)));
    }

    #[test]
    fn sample3() {
        assert_eq!(-6, calculate_frequency(&vec!(-1, -2, -3)));
    }

    #[test]
    fn sample4() {
        assert_eq!(0, first_double_frequency(&vec!(1, -1)));
    }

    #[test]
    fn sample5() {
        assert_eq!(10, first_double_frequency(&vec!(3, 3, 4, -2, -4)));
    }

    #[test]
    fn sample6() {
        assert_eq!(5, first_double_frequency(&vec!(-6, 3, 8, 5, -6)));
    }

    #[test]
    fn sample7() {
        assert_eq!(14, first_double_frequency(&vec!(7, 7, -2, -7, -4)));
    }
}
