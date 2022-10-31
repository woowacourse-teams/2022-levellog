import { Component, PropsWithChildren } from 'react';

import { AxiosError } from 'axios';

import Error from 'pages/status/Error';

class ErrorBoundary extends Component<PropsWithChildren, ErrorBoundaryState> {
  constructor(props: PropsWithChildren) {
    super(props);
    this.state = {
      showError: false,
    };
    this.handleErrorReset = this.handleErrorReset.bind(this);
  }

  handleErrorReset() {
    this.setState({ showError: false });
  }

  static getDerivedStateFromError(err: Error) {
    if (err.name === 'TypeError') {
      return {
        showError: true,
      };
    }

    if (err instanceof AxiosError) {
      if (err.response?.status === 500 || err.response?.status === 404) {
        return {
          showError: true,
        };
      }
    }

    return {
      showError: true,
    };
  }

  render() {
    const { children } = this.props;

    if (this.state.showError) {
      return <Error onClick={this.handleErrorReset} />;
    }

    return children;
  }
}

interface ErrorBoundaryState {
  showError: boolean;
}

export default ErrorBoundary;
