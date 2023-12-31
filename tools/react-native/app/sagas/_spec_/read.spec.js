
import { put, call } from 'redux-saga/effects';

import { requestArticleList } from '../read';
import RequestUtil from '../../utils/RequestUtil';
import { WEXIN_ARTICLE_LIST } from '../../constants/Urls';
import { fetchArticleList, receiveArticleList } from '../../actions/read';

/* global expect */
describe('read saga tests', () => {
  const {
    isRefreshing, loading, typeId, isLoadMore, page
  } = {
    isRefreshing: false,
    loading: false,
    typeId: 2,
    isLoadMore: false,
    page: 1
  };
  const generator = requestArticleList(
    isRefreshing,
    loading,
    typeId,
    isLoadMore,
    page
  );
  const mockArticleList = {
    showapi_res_body: {
      pagebean: {
        contentlist: []
      }
    }
  };
  const step = input => generator.next(input).value;

  it(`should put(fetchArticleList(${isRefreshing}, ${loading}, ${isLoadMore}))`, () => {
    const next = step();
    expect(next).toEqual(put(fetchArticleList(isRefreshing, loading, isLoadMore)));
  });

  it(`should call(request, ${WEXIN_ARTICLE_LIST}?typeId=${typeId}&page=${page}, 'get')`, () => {
    const next = step();
    expect(next).toEqual(call(
      RequestUtil.request,
      `${WEXIN_ARTICLE_LIST}?typeId=${typeId}&page=${page}`,
      'get'
    ));
  });

  it(`should put(receiveArticleList(contentlist, ${typeId}))`, () => {
    const next = step(mockArticleList);
    expect(next).toEqual(put(receiveArticleList(
      mockArticleList.showapi_res_body.pagebean.contentlist,
      typeId
    )));
  });

  it('should be done', () => {
    const done = generator.next().done;
    expect(done).toEqual(true);
  });
});
