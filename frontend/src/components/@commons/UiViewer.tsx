import '@toast-ui/editor/dist/toastui-editor.css';
import { Viewer } from '@toast-ui/react-editor';

const UiViewer = ({ content }: UiViewerProps) => {
  return <Viewer initialValue={content} />;
};

interface UiViewerProps {
  content: string;
}

export default UiViewer;
