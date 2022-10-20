import '@toast-ui/editor/dist/toastui-editor.css';
import { Viewer } from '@toast-ui/react-editor';

const UiViewer = ({ content }: UiViewerProps) => {
  return content ? <Viewer initialValue={content} /> : <p>불러오는 중</p>;
};

interface UiViewerProps {
  content: string | undefined;
}

export default UiViewer;
