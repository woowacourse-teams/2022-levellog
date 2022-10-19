import { convertDateAndTime, convertFirstWordFinalConsonant, debounce } from './util';
import { describe, expect, test, jest } from '@jest/globals';

jest.useFakeTimers();

describe('debounce 함수 동작 확인', () => {
  test('단 시간에 debounce를 여러번 호출해도 한번만 호출되야 한다.', () => {
    const func = jest.fn();

    for (let i = 0; i < 1000; i++) {
      debounce.action({ func });
      jest.advanceTimersByTime(50);
    }

    jest.runAllTimers();

    expect(typeof debounce.action).toBe('function');
    expect(func).toHaveBeenCalledTimes(1);
  });

  test('timer를 설정한 만큼동안 여러번 호출해도 한번만 호출되야 한다.', () => {
    const func = jest.fn();

    debounce.action({ func, timer: 3000 });
    jest.advanceTimersByTime(2000);
    debounce.action({ func, timer: 3000 });

    jest.runAllTimers();

    expect(typeof debounce.action).toBe('function');
    expect(func).toHaveBeenCalledTimes(1);
  });
});

describe('앞 글자가 받침이 있으면 이, 없으면 가를 붙여주는 함수 동작 확인', () => {
  test('앞 글자가 받침이 있으면 이를 붙여서 반환해야 한다.', () => {
    const word = '결';
    const convertWord = convertFirstWordFinalConsonant({ word });

    expect(convertWord).toBe(`${word}이`);
  });

  test('앞 글자가 받침이 없으면 가를 붙여서 반환해야 한다.', () => {
    const word = '결이';
    const convertWord = convertFirstWordFinalConsonant({ word });

    expect(convertWord).toBe(`${word}가`);
  });
});

describe('년월일시간 형식을 변경하는 함수 동작 확인', () => {
  test('2022-10-12T14:17:00 =>  2022년 10월 12일 14시 17분', () => {
    const startAt = '2022-10-12T14:17:00';
    const convertStartAt = convertDateAndTime({ startAt });

    expect(convertStartAt).toBe('2022년 10월 12일 14시 17분');
  });
});
