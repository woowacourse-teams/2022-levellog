import { createContext, Dispatch, useReducer } from 'react';

const initialState: string[] = [];

const reducer = (prevState: State, action: Action): string[] => {
  switch (action.type) {
    case 'add':
      return [...prevState, action.message];
    case 'delete':
      return prevState.slice(1, prevState.length);
    default:
      throw new Error();
  }
};

export const SnackbarContext = createContext<State>(initialState);
export const SnackbarDispatchContext = createContext<SnackbarDispatchType>(() => {});

export const SnackbarProvider = ({ children }: SnackbarProviderProps) => {
  const [snackbars, dispatch] = useReducer<Reducer<State, Action>>(reducer, initialState);

  return (
    <SnackbarContext.Provider value={snackbars}>
      <SnackbarDispatchContext.Provider value={dispatch}>
        {children}
      </SnackbarDispatchContext.Provider>
    </SnackbarContext.Provider>
  );
};

type Reducer<S, A> = (prevState: S, action: A) => S;

type State = string[];
type Action = { type: 'add'; message: string } | { type: 'delete' };

type SnackbarDispatchType = Dispatch<Action>;

interface SnackbarProviderProps {
  children: JSX.Element;
}
